#pragma strict

var speed:float = 3.0; 
var jump:boolean;
var slide:boolean;

var animator: Animator;
function OnGUI()
{
	GUI.Box (Rect (10,10,100,30), "Time: "+Time.time);
}

function Start()
{
animator = GetComponent("Animator");

jump = false;

}


function FixedUpdate () 
{
	MoveCharacter();
}

function MoveCharacter()
{

if (Input.GetKey(KeyCode.W))
{
	
	animator.SetBool("jump", true);
jump = true;
}
else 
{
animator.SetBool("jump", false);
jump = false;
}

if (Input.GetKey(KeyCode.S))
{
	
	animator.SetBool("slide", true);
slide = true;
}
else 
{
animator.SetBool("slide", false);
slide = false;
}
}
