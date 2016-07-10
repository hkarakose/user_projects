#pragma strict

//the speed that the ship will move 
var speed:float = 3.0; 


function Start () 
{

}

//Move the character each frame
function Update () 
{
	MoveCharacter();
}

//function to move character
function MoveCharacter()
{

//If we press “A” then ship moves left
if (Input.GetKey(KeyCode.A))
{
	transform.Translate(Vector3.left * speed * Time.deltaTime);
}

// If we press “D” then ship moves right
if (Input.GetKey(KeyCode.D))
{
	transform.Translate(Vector3.right * speed * Time.deltaTime);
}
}
