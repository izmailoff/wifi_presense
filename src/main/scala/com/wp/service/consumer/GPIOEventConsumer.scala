package com.wp.service.consumer

import com.pi4j.io.gpio.{GpioFactory, RaspiPin, GpioPinDigitalOutput}
import com.wp.messages.EventMessages.ClientPacket

class GPIOEventConsumer
  extends EventConsumer {

  var lastSeen = 0L
  //ClientPacket(4299709,ac:7b:a1:e4:b8:d0,List(-51))
  //ClientPacket(4299773,00:26:75:d8:00:68,List(-63))
  //ClientPacket(4299809,74:d0:2b:67:11:66,List(-77))
  val monitoredAddress = "D0:22:BE:7B:F2:09"
  val assignedPin = GPIOController.pin0
  val absenceIntervalMillis = 30 * 1000

  override def process(event: ClientPacket): Unit = {
    import event._
    if(clientAddress.toUpperCase == monitoredAddress) { // ifx upper case in case class constructor/apply
      println(s"Seen monitored client [$monitoredAddress]. Turning the light ON.")
      lastSeen = System.currentTimeMillis
      assignedPin.high()
    }
    else if(System.currentTimeMillis > (lastSeen + absenceIntervalMillis)) {
      println(s"Haven't seen monitored client [$monitoredAddress] for more than $absenceIntervalMillis millis. " +
        "Turning the light OFF.")
      assignedPin.low()
    }
  }

}

object GPIOController {
  val gpio = GpioFactory.getInstance()
  val pin0 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "pin0")
  val pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "pin1")
  val pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "pin2")
  val pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "pin3")
  val pin4 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "pin4")
  val pin5 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, "pin5")
  val pin6 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "pin6")
  val pin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07, "pin7")
  val pin8 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "pin8")
  val pin9 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "pin9")
  val pin10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_10, "pin10")
  val pin11 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_11, "pin11")
  val pin12 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, "pin12")
  val pin13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, "pin13")
  val pin14 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "pin14")
  val pin15 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_15, "pin15")
  val pin16 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "pin16")
  //val pin17 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_17, "pin17")
  val digitalOutPins: Vector[GpioPinDigitalOutput] = Vector(pin1, pin2, pin3, pin4, pin5, pin6)

  // TODO: add on shutdown restore pin state to off like:
  // myLed.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
}
