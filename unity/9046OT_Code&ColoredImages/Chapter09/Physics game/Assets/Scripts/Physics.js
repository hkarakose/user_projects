#pragma strict

var cannonball:Rigidbody2D;

var power:float = 30.0; 

function OnGUI()
{

	GUI.Box (Rect (10,10,100,30), "Power: "+power);
}

function FixedUpdate()
{

	Cannonballs();
}

function Cannonballs()
{

	if (Input.GetKey(KeyCode.W))
	{
	
		if (power <= 39)
		{
		
			power = power + 1;
		}
	}
	
	if (Input.GetKey(KeyCode.S))
	{
	
		if (power >= 21)
		{
		
			power = power - 1;
		}
	}
	
	if (Input.GetKeyDown("space"))
	{
	
		SpawnCannonballs();	
	}
	
}

function SpawnCannonballs()
{

	var cannonballInstance:Rigidbody2D; 

	cannonballInstance = Instantiate(cannonball, Vector3(-76,-26,70), Quaternion.Euler(new Vector3(0,0,0)));

	cannonballInstance.transform.Rotate(0,0,54);
	
	cannonballInstance.velocity = new Vector2(power,power);
}

