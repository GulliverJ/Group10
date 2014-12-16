const int pingPin = 22;
byte byteRead = 3;
long cm = -1;
int initialise = 1;  // Used to ensure only one reading is sent through

void setup() {
  Serial.begin(9600);
  Serial2.begin(115200);
}

void loop()
{
  
  if( initialise > 0 ) {    // Opens communication with the port
    Serial.println("Start");
    initialise = 0;
  }
    
  if( cm > 0 ){             // Sends the value only if a reading has been taken.
    Serial.println(cm);
    cm = -1;
  }

}

void serialEvent() {
    
    byteRead = Serial.read();
    long duration = 0;
    
    for(int i = 0; i < 100; i++) {
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
    duration = duration/100;
    
    cm = microsecondsToCentimeters(duration);

}

//Gives a fairly accurate conversion to centimetres
long microsecondsToCentimeters(long microseconds)
{
  return (long)microseconds/55;
}

