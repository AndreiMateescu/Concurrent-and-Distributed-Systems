#!/usr/bin/python3

import hashlib
import threading
import time
import config

passwordToHack = '87C4226D9037DB6FD07EE2F084E5D84A67F379D7CC025CD22F40E91ED5F9CEDF'
passwordToHack = passwordToHack.lower()

class myThread (threading.Thread):
   def __init__(self, threadID, name, startNumber, endNumber):
      threading.Thread.__init__(self)
      self.threadID = threadID
      self.name = name
      self.startNumber = startNumber
      self.endNumber = endNumber

   def run(self):
      print ("Starting " + self.name)
      rangeSha256Algorithm(self.startNumber, self.endNumber, self.name)

def rangeSha256Algorithm (startNumber,endNumber,threadName):
   for i in range(startNumber-1,endNumber):
      myHash =  hashlib.sha256(bytes(str(i), encoding='utf-8')).hexdigest()
      if myHash == passwordToHack or config.findFlag == True:
         if config.findFlag == True:
            print ("Stoping  %s" % (threadName))
            break
         config.result = i
         print ("Stoping  %s" % (threadName))
         config.findFlag = True
         break

threads = []

for i in range(1,40):
   startNumber = round(float(10000000) + float(i - 1) * (float(89999999) / float(42)))
   endNumber = round(float(10000000) + float(i) * (float(89999999) / float(42)))

   name = "Thread-"+ str(i)
   threads.append(myThread(i, name, startNumber, endNumber))

   threads[i-1].start()

for t in threads:
   t.join()
print ("Exiting Main Thread")

print (config.result)