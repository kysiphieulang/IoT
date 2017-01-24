#include <ESP8266WiFi.h>        /* Library does task wifi*/
#include <ESP8266WebServer.h>   /* Library create server */
#include <EEPROM.h>             /* Library read/write EEPROM */

/* Define variable */
boolean led_state = 0;

String ssid;
String pass;

String s = "";
char statusCode;

boolean connectInternet = false;
boolean enterBoot = false;
boolean isDone = true;

const char* host = "luutruthongtin.net46.net";
String url = "/post_data.html";
String url_post = "/post.php";

boolean notFoundCommand = false;
String received[] = {"eh", "hey", "control something"};
String answer[]   = {"Yes?", "Im here sir.", "What do you want?"};
ESP8266WebServer server(80);

void setup() {
  pinMode(13, OUTPUT);
  // put your setup code here, to run once:
  Serial.begin(115200);
  EEPROM.begin(512);
  delay(10);

  int c = 0;
  Serial.println("Press any key to open setting hotspot.");  
  while ( c < 20 && !enterBoot) {   
    if(Serial.available())  enterBoot = true;
    delay(500);
    Serial.print(".");    
    c++;
  }
  
  if(!enterBoot)
    connectWifi();
  else 
    APMode();
}

void loop() {
  // put your main code here, to run repeatedly:
  server.handleClient();

  if(connectInternet && isDone){
    isDone = false;
    readWeb();
  } 
}
void connectWifi(){
  getFromEEPROM();
  Serial.println("Startup. Get password from EEPROM");
  WiFi.begin(ssid.c_str(), pass.c_str());
  if (testWifi()) {
    connectInternet = true;
    Serial.println();
    Serial.println("Connected");      
    delay(1000);
    digitalWrite(13, HIGH);
    return;
  }
  else 
    APMode();
}

bool testWifi(void) {
  int c = 0;
  Serial.println("Waiting for Wifi to connect");  
  while ( c < 20 ) {   
    if (WiFi.status() == WL_CONNECTED) { return true;} 
    delay(500);
    Serial.print(".");    
    c++;
  }
  Serial.println("");
  Serial.println("Connect timed out, opening AP");
  return false;
} 

void APMode(){
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  Serial.println(WiFi.softAP("ESP_AccessPoint") ? "Ready" : "Failed!");
  IPAddress serverIP = WiFi.softAPIP();
  Serial.print("Your IP :");    Serial.println(serverIP);
  
  server.on("/", [](){  //Main page
    s = "";
    s += "<!DOCTYPE HTML>\r\n<html><body>\r\n";
    s += "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>";
    s += "<head><style>.labelClass{float: left;width: 113px;}</style></head>";
    s += "<html><h1>Cài đặt mạng cho ESP8266</h1>";
    s += "</p><form action='setting' method='get'>"; //go to setting
    s += "<span class='labelClass'>SSID: </span><input type='text' name='ssid'>";
    s += "</p><span class='labelClass'>Password: </span><input type='text' name='pass'>";
    s += "</p><button name='subject' type='submit'>Lưu thiết lập</button>";
    s += "</p><a href='http://192.168.4.1/ircontrol'>> Điều khiển qua IR</a>";
    s += "</p><a href='http://192.168.4.1/update'>> Cập nhật chương trình</a>";
    s += "</p><a href='http://192.168.4.1/reset'>> Khởi động lại thiết bị</a></p>";
    s += "</form></body>";
    server.send(200, "text/html", s);
  });

  server.on("/setting", [](){   //Setting
    ssid = server.arg("ssid");
    pass = server.arg("pass"); 
    if (ssid.length() > 0 && pass.length() > 0) {
      saveToEEPROM();
      s = "";
      s += "<!DOCTYPE HTML>\r\n<html><body>\r\n";
      s += "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>";
      s += "</p><form action='reset' method='get' id='reset'>";  //go to reset
      s += "</p>Đã lưu thiết lập. Khởi động lại để áp dụng. ";
      s += "</p><button name='check' type='submit'>Khởi động lại chip</button>";
      s += "</p><a href='http://192.168.4.1/'>< Quay về trang chủ</a>";
      s += "</form></body>";
      statusCode = 200;
    } 
    else {
      s = "Action failed!";
      statusCode = 404;
      Serial.println("Sending 404");
    }
    server.send(statusCode, "text/html", s); 
  });

  server.on("/reset", [](){   //Reset
    s = "";
    s += "<!DOCTYPE HTML>\r\n<html><body>\r\n";
    s += "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>";                                                                     
    s += "</p>Module đang reset. Chờ giây lát.";
    s += "</form></body>";
    server.send(200, "text/html", s);
    delay(500);
    ESP.reset();
  });

  server.begin();
  Serial.println("HTTP server started");
}
void readWeb(){
  WiFiClient client;
  const int httpPort = 80;
  if (!client.connect(host, httpPort)) {
    Serial.println("Connection failed. Restart...");
    delay(1000);
    ESP.reset();
    return;
  }
  client.print(String("GET ") + url + " HTTP/1.1\r\n" +
               "Host: " + host + "\r\n" + 
               "Connection: close\r\n\r\n");
  delay(500);
    
  String data;
  while(client.available()){
    String line = client.readStringUntil('\r');
    data = data + line;
  }
  extractCommand(data);
}
void sendWeb(String data){
  WiFiClient client;
  const int httpPort = 80;
  if (!client.connect(host, httpPort)) {
    Serial.println("Connection failed");
    return;
  }
  client.print(String("POST ") + url_post + " HTTP/1.1\r\n" +
                 "Host: " + host + "\r\n" +
                 //"Connection: close\r\n" +
                 "Content-Type: application/x-www-form-urlencoded\r\n" +
                 "Content-Length: " + data.length()+2 + "\r\n" +
                 "\r\n" + // This is the extra CR+LF pair to signify the start of a body
                 data + "\n");
  delay(500);
}
void getFromEEPROM(){
  ssid = read_eeprom(0, 30);
  Serial.print("Reading EEPROM ssid: "); Serial.println(ssid);
  
  pass = read_eeprom(30, 50);
  Serial.print("Reading EEPROM password: "); Serial.println(pass);
}
void saveToEEPROM(){
  Serial.println("clearing eeprom");
  for (int i = 0; i < 95; ++i) 
    EEPROM.write(i, 0); 
          
  ssid = server.arg("ssid"); 
  Serial.println("writing eeprom ssid.."); 
  write_eeprom(ssid, 0);
      
  pass = server.arg("pass"); 
  Serial.println("writing eeprom pass.."); 
  write_eeprom(pass, 30);
  
  EEPROM.commit();
}

String read_eeprom(int pos, int max_len){
  String temp_data;
  for (int i = pos; i < max_len; ++i){
    temp_data += char(EEPROM.read(i));
  }
  return temp_data;
}

void write_eeprom(String data, int pos){
  Serial.print("Write: ");
  for (int i = 0; i < data.length(); ++i){
     EEPROM.write(pos+i, data[i]);
     Serial.print(data[i]); 
  }
  Serial.println();
}
void extractCommand(String response){
  int index = response.indexOf("|");
  int lastindex = response.lastIndexOf("|");
  String command = response.substring(index+1, lastindex);
  Serial.println(command);
  botChat(command);
}
void botChat(String data){
  notFoundCommand = true;
  for(int i=0; i<sizeof(received)-1;i++){
    if(data == received[i]){
      String send_data = "message=|" + answer[i] + "|";
      sendWeb(send_data);
      notFoundCommand = false;  
    }
  }
  if(notFoundCommand)   sendWeb("message=|I dont understand. Do you want to add new command?|");
  led_state = !led_state;
  digitalWrite(13, led_state);
  delay(100);
  isDone = true;    
}

