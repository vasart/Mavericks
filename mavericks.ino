#include <Servo.h> 
#include <WiFi.h>

#define servo_right_pin 14
#define servo_left_pin 15
#define sonar_echo_pin 16
#define sonar_trig_pin 17

char ssid[] = "Maverick";

Servo servo_right;
Servo servo_left;

const int LEFT_FRWD = 174;
const int LEFT_HALF = 105;
const int LEFT_STOP = 95;
const int LEFT_HLBK = 85;
const int LEFT_BACK = 5;

const int RIGHT_FRWD = 0;
const int RIGHT_HALF = 100;
const int RIGHT_STOP = 110;
const int RIGHT_HLBK = 120;
const int RIGHT_BACK = 179;

const int PORT = 6789;

int wifi_status = WL_IDLE_STATUS;
WiFiServer server(6789);

void setup() {
  // Servos
  servo_right.attach(servo_right_pin);
  servo_left.attach(servo_left_pin);
  servo_stop();

  pinMode(sonar_trig_pin, OUTPUT);
  pinMode(sonar_echo_pin, INPUT);

  Serial.begin(9600);

  while (wifi_status != WL_CONNECTED) { 
    Serial.print("Attempting to connect ");
    Serial.println(ssid);

    // Connect to network.
    wifi_status = WiFi.begin(ssid);
    // wait 2 seconds for connection:
    delay(2000);
  }

  server.begin();
  print_wifi_status();   
}

void loop() 
{
  WiFiClient client = server.available();   // listen for incoming clients    

  if (client)
  {
    while (client.connected()) 
    {             
      if (client.available()) {
        char command = client.read();
        Serial.write(command);
        process_command(command);
        client.print(command);
      }
    }
    client.stop();
  }
  delay(1);
}

void process_command(char command) {
  if(command == 'f') // going forward
  {
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

  if(command == 'b') // going backward
  {
    servo_right.write(RIGHT_BACK);
    servo_left.write(LEFT_BACK);
  }

  if(command == 'a') // turn right on place
  {
    servo_right.write(RIGHT_BACK);
    servo_left.write(LEFT_FRWD);
  }

  if(command == 'c') // turn left on place
  {
    servo_right.write(RIGHT_FRWD);
    servo_left.write(LEFT_BACK);
  }

  if(command == 'r') // circle turn right
  {
    servo_right.write(RIGHT_HALF);
    servo_left.write(LEFT_FRWD);
  }

  if(command == 'l') // circle turn left
  {
    servo_right.write(RIGHT_FRWD);
    servo_left.write(LEFT_HALF);
  }

  if(command == 'd') // circle turn right back
  {
    servo_right.write(RIGHT_HLBK);
    servo_left.write(LEFT_BACK);
  }

  if(command == 'e') // circle turn left back
  {
    servo_right.write(RIGHT_BACK);
    servo_left.write(LEFT_HLBK);
  }

  if(command == 's') // stop
  {
    servo_stop();
  }
}

void servo_stop()
{
  servo_right.write(RIGHT_STOP);
  servo_left.write(LEFT_STOP);
}

int find_sonar_distance()
{
  digitalWrite(sonar_trig_pin, LOW);
  delayMicroseconds(2);
  digitalWrite(sonar_trig_pin, HIGH);
  delayMicroseconds(10);
  digitalWrite(sonar_trig_pin, LOW);
  int duration = pulseIn(sonar_echo_pin, HIGH);
  return (duration / 2) / 29.1;
}

void print_wifi_status() 
{
  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);
}

