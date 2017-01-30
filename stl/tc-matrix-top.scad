//
//  LedMatrix
//  part of Let's make series
//
//  The Cave, 2017
//  https://thecave.cz
//
//  Licensed under MIT License (see LICENSE file for details)
//

$innerX = 45;
$innerY = 35;
$outerZ = 13;
$disp = 33.5;
$dispZ = 7.7;
$wall = 1.6;
$outerDia = 5;
$dentZ = 3;
$holder = 2;

difference() {
    translate([$outerDia/2,$outerDia/2,$outerDia/2]) minkowski() {
        sphere(d=$outerDia,$fn=50);
        cube([$innerX+$wall*2-$outerDia,$innerY+$wall*2-$outerDia,$outerZ+$outerDia*2]);
    }
    translate([$wall,$wall,$wall]) cube([$innerX,$innerY,$outerZ*2]);
    translate([-1,-1,$outerZ]) cube([$innerX*2,$innerY*2,$outerZ*2]);
    translate([$wall/2+($outerDia-$wall)/2,$wall/2+($outerDia-$wall)/2,$outerZ-$dentZ]) minkowski() {
        cube([$innerX+$wall-($outerDia-$wall),$innerY+$wall-($outerDia-$wall),$outerZ]);
        cylinder(d=$outerDia-$wall,h=$outerZ,$fn=50);
    }
    translate([$wall+($innerX-$disp)/2,$wall+($innerY-$disp)/2,-1]) cube([$disp,$disp,$wall*2]);
    

}

translate([($innerX-$disp)/2-($holder-$wall),$wall-0.001,$wall/2]) {
    cube([$holder,$holder*2,$dispZ]);
    translate([$holder,0,$dispZ-$holder/2]) polyhedron(points=[[0,0,0],[$holder*2,0,0],[0,$holder*2,0],[0,0,$holder/2],[$holder*2,0,$holder/2],[0,$holder*2,$holder/2]], faces=[[0,1,2],[0,3,1],[3,4,1],[0,2,5],[3,0,5],[3,5,4],[1,4,5],[1,5,2]]);
}

translate([($innerX-$disp)/2-($holder-$wall),$innerY+$wall-$holder*2,$wall/2]) {
cube([$holder,$holder*2,$dispZ]);
    translate([$holder,$holder*2,$dispZ-$holder/2]) rotate([0,0,270]) polyhedron(points=[[0,0,0],[$holder*2,0,0],[0,$holder*2,0],[0,0,$holder/2],[$holder*2,0,$holder/2],[0,$holder*2,$holder/2]], faces=[[0,1,2],[0,3,1],[3,4,1],[0,2,5],[3,0,5],[3,5,4],[1,4,5],[1,5,2]]);

}
translate([$innerX+$wall-($innerX-$disp)/2,$wall-0.001,$wall/2]) {
    cube([$holder,$holder*2,$dispZ]);
    translate([0,0,$dispZ-$holder/2]) rotate([0,0,90]) polyhedron(points=[[0,0,0],[$holder*2,0,0],[0,$holder*2,0],[0,0,$holder/2],[$holder*2,0,$holder/2],[0,$holder*2,$holder/2]], faces=[[0,1,2],[0,3,1],[3,4,1],[0,2,5],[3,0,5],[3,5,4],[1,4,5],[1,5,2]]);
}
translate([$innerX+$wall-($innerX-$disp)/2,$innerY+$wall-$holder*2,$wall/2]) {
    cube([$holder,$holder*2,$dispZ]);
    translate([0,$holder*2,$dispZ-$holder/2]) rotate([0,0,180]) polyhedron(points=[[0,0,0],[$holder*2,0,0],[0,$holder*2,0],[0,0,$holder/2],[$holder*2,0,$holder/2],[0,$holder*2,$holder/2]], faces=[[0,1,2],[0,3,1],[3,4,1],[0,2,5],[3,0,5],[3,5,4],[1,4,5],[1,5,2]]);
}

