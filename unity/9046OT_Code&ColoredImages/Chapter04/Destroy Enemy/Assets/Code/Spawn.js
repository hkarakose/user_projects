#pragma strict

var bullet:Rigidbody2D;				// Prefab of the bullet.
var speed:float = 20f;				// The speed the bullet will fire at.

function Update ()
{
	var bulletInstance:Rigidbody2D;
	
	// If the fire button is pressed...
	if(Input.GetButtonDown("Fire1")&& Hit.hit == false)
	{
		// ... instantiate the bullet facing right and set it's velocity to the right. 
		bulletInstance = Instantiate(bullet, transform.position, Quaternion.Euler(new Vector3(0,0,0)));
		bulletInstance.velocity = new Vector2(speed, 0);
		bulletInstance.name = "Bullet";
	}
}



	
