const int pingPin = 22;
byte byteRead = 3;
long cm = -1;
int five = 5;
int initialise = 1;

void setup() {
  // initialize serial communication:
  Serial.begin(9600);
  Serial2.begin(115200);
}

void loop()
{
  // establish variables for duration of the ping, 
  // and the distance result in inches and centimeters:
  if( initialise > 0 ) {
    Serial.println("Start");
    initialise = 0;
  }
    
  if( cm > 0 ){
    //Serial2.println(cm);
    Serial.println(cm);
    cm = -1;
  }
  //Serial.println("test");
  //delay(500);
}

void serialEvent() {
  //while (Serial.available()) {
    
    byteRead = Serial.read();
    long duration = 0;
    
    for(int i = 0; i < 10; i++) {
      // The PING))) is triggered by a HIGH pulse of 2 or more microseconds.
      // Give a short LOW pulse beforehand to ensure a clean HIGH pulse:
      pinMode(pingPin, OUTPUT);
      digitalWrite(pingPin, LOW);
      delayMicroseconds(2);
      digitalWrite(pingPin, HIGH);
      delayMicroseconds(5);
      digitalWrite(pingPin, LOW);

      // The same pin is used to read the signal from the PING))): a HIGH
      // pulse whose duration is the time (in microseconds) from the sending
      // of the ping to the reception of its echo off of an object.
      pinMode(pingPin, INPUT);
      duration += pulseIn(pingPin, HIGH);
    }
    duration = duration/10;
    
    cm = microsecondsToCentimeters(duration);
 // }
}

//Gives a fairly accurate conversion to centimetres
long microsecondsToCentimeters(long microseconds)
{
  return (long)microseconds/55;
}

