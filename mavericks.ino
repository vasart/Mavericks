#include <Servo.h> 
#include <WiFi.h>

#define servo_right_pin 14
#define servo_left_pin 15
#define sonar_echo_pin 16
#define sonar_trig_pin 17
#define listen_port 6789;

char ssid[] = "CMU";

Servo servo_right;
Servo servo_left;

const int LEFT_FRWD = 174;
const int LEFT_HALF = 110;
const int LEFT_STOP = 95;
const int LEFT_HLBK = 80;
const int LEFT_BACK = 5;

const int RIGHT_FRWD = 0;
const int RIGHT_HALF = 95;
const int RIGHT_STOP = 110;
const int RIGHT_HLBK = 125;
const int RIGHT_BACK = 179;

int wifi_status = WL_IDLE_STATUS;
WiFiServer server(listen_port);

void setup() {
  servo_right.attach(servo_right_pin);
  servo_left.attach(servo_left_pin);
  servo_stop();

  pinMode(sonar_trig_pin, OUTPUT);
  pinMode(sonar_echo_pin, INPUT);

  Serial.begin(9600);
  Serial.println("Hello");

  setupWiFi();
}

void loop() {
  WiFiClient client = server.available();

  if (client) {
    while (client.connected()) {
      if (client.available()) {
        char command = client.read();
        Serial.write(command);
        process_command(command);
        //client.print(command);
      }
      delay(100);
    }
  } else if (server.status() == 0) {
    Serial.println("Server down");
    servo_stop();
  }

  client.stop();
  delay(10);
  // Uncomment this if you are facing a bug with WiFi shield.
  // That is, when server is dying after 20s of inactivity.
  //server.write(' ');
}

/*
//Test that commands work from serial input
void serialEvent() {
  while (Serial.available()) {
    char inChar = (char) Serial.read(); 
    Serial.println(inChar);
    process_command(inChar); 
  }
}
*/

void process_command(char command) {
  if(command == 'f') {
    // going forward
    //int distance = find_sonar_distance();
    //if (distance > 0)
    //{
    //  if (distance > 5)
    //  {
    servo_right.write(RIGHT_FRWD);
    servo_left.write(LEFT_FRWD);
    //  } else {
    //    servo_stop();
    //  }
    //}
  }

  if(command == 'b') {
    // going backward
    servo_right.write(RIGHT_BACK);
    servo_left.write(LEFT_BACK);
  }

  if(command == 'a') {
    // turn right on place
    servo_right.write(RIGHT_BACK);
    servo_left.write(LEFT_FRWD);
  }

  if(command == 'c') {
    // turn left on place
    servo_right.write(RIGHT_FRWD);
    servo_left.write(LEFT_BACK);
  }

  if(command == 'r') {
    // circle turn right
    servo_right.write(RIGHT_HALF);
    servo_left.write(LEFT_FRWD);
  }

  if(command == 'l') {
    // circle turn left
    servo_right.write(RIGHT_FRWD);
    servo_left.write(LEFT_HALF);
  }

  if(command == 'd') {
    // circle turn right back
    servo_right.write(RIGHT_HLBK);
    servo_left.write(LEFT_BACK);
  }

  if(command == 'e') {
    // circle turn left back
    servo_right.write(RIGHT_BACK);
    servo_left.write(LEFT_HLBK);
  }

  if(command == 's') {
    // stop moving
    servo_stop();
  }
}

void servo_stop() {
  servo_right.write(RIGHT_STOP);
  servo_left.write(LEFT_STOP);
}

void setupWiFi() {
  WiFi.disconnect();
  wifi_status = WL_IDLE_STATUS;
  while (wifi_status != WL_CONNECTED) { 
    Serial.print("Trying to connect network with SSID: ");
    Serial.println(ssid);

    wifi_status = WiFi.begin(ssid);
    delay(2000);
  }

  Serial.println("Connected");
  server.begin();
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);
}

int find_sonar_distance() {
  digitalWrite(sonar_trig_pin, LOW);
  delayMicroseconds(2);
  digitalWrite(sonar_trig_pin, HIGH);
  delayMicroseconds(10);
  digitalWrite(sonar_trig_pin, LOW);
  int duration = pulseIn(sonar_echo_pin, HIGH);
  return (duration / 2) / 29.1;
}

