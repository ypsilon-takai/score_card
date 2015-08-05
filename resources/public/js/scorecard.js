//////////////////////////////////////////
// javascript for score card

//////////////////
// for score card body
function ShotDown(tgtname) {
    valnow = parseInt(document.getElementById(tgtname).value);
    document.getElementById(tgtname).value = (valnow <= 0)? 0 : (valnow -1);
}

function ShotUp(tgtname) {
    valnow = parseInt(document.getElementById(tgtname).value);
    document.getElementById(tgtname).value = valnow + 1;
}


    