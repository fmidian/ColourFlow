# ColourFlow

ColourFlow creates images of random colour gradients. The first pixel in the middle of the canvas is filled randomly with a colour. Afterwards the colour "flows" in all directions until the whole canvas is filled. Screenshots, explanation of further functionalities and information about local properties can be found below.


![pictureOfFlow](screenshots/flow.PNG?raw=true "Screenshot during flow")

![pictureReady](screenshots/ready.PNG?raw=true "Screenshot during flow")

## Features
Using the "Search"-Box it's possible to integrate an image matching the input in the colour gradient. An example with a search for "charlie_chaplin" is shown below.
![pictureReady](screenshots/chaplin.PNG?raw=true "Screenshot during flow")

The kind of the flowage-animation can be changed using the checkbox "flowage". An example of a different flowage type is shown below.
![pictureReady](screenshots/flowage.PNG?raw=true "Screenshot during flow")

Also the user can modify the changerate of random variation.

## Local Properties
In order to use the "Search"-Feature you have to use a Google-API-Key and a corresponding SeachEngine-ID. Add these two strings to the GoogleApi.properties file.
