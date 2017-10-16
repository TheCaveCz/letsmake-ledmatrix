//
//  LedMatrix
//  part of Let's make series
//
//  The Cave, 2017
//  https://thecave.cz
//
//  Licensed under MIT License (see LICENSE file for details)
//

$innerX = 43.5;
$innerY = 33.8;
$outerZ = 15;
$wall = 1.6;
$outerDia = 5;
$dentZ = 2.5;

$usbWidth = 8;
$usbHeight = 4;
$arduinoWidth = 19;
$buttonDia = 7.2;
$usbY = 3.7;
$usbX = ($arduinoWidth-$usbWidth)/2;


difference() {
    translate([$outerDia/2,$outerDia/2,$outerDia/2]) minkowski() {
        sphere(d=$outerDia,$fn=50);
        cube([$innerX+$wall*2-$outerDia,$innerY+$wall*2-$outerDia,$outerZ+$outerDia*2]);
    }
    translate([$wall,$wall,$wall]) cube([$innerX,$innerY,$outerZ*2]);
    translate([-1,-1,$outerZ-$dentZ]) cube([$innerX*2,$innerY*2,$outerZ*2]);
    
    translate([$innerX+$wall-1,$wall+$usbX,$wall+$usbY]) cube([$wall+2,$usbWidth, $usbHeight]);
    translate([$wall+$innerX-1,$wall+$innerY*0.77,$wall+($outerZ-$dentZ)/2]) rotate([0,90,0]) cylinder(d=$buttonDia,h=$wall+2,$fn=50);
}

translate([$wall,$wall+$arduinoWidth,$wall]) cube([$innerX*0.1, 1.2, 4]);
translate([$wall+$innerX*0.9,$wall+$arduinoWidth,$wall]) cube([$innerX*0.1, 1.2, 4]);



translate([$outerDia/2,$outerDia/2,$outerZ-$dentZ]) difference() {
    minkowski() {
        cube([$innerX+$wall-($outerDia-$wall),$innerY+$wall-($outerDia-$wall),$dentZ/2]);
        cylinder(d=$outerDia-$wall-0.3,h=$dentZ/2,$fn=50);
    }
    translate([-$outerDia/2+$wall,-$outerDia/2+$wall,-1]) cube([$innerX,$innerY,$dentZ+2]);
}
