#pragma strict
var grime:Rigidbody2D;
var speed:float = -3.0; 

function Start () {

}

function Update () {

}

Invoke("SpawnGrime", 2);

function SpawnGrime()
{
	var grimeInstance:Rigidbody2D; 

grimeInstance = Instantiate(grime, Vector3(Random.Range(-8, 8),7,0), Quaternion.Euler(new Vector3(0,0,0)));
grimeInstance.name = "Grime(Clone)";
grimeInstance.velocity = new Vector2(0, speed);

}

/*function OnCollisionEnter2D(other:Collision2D)
{

  	if(other.gameObject.name=="Spongy")
  	{
  	
    Destroy(gameObject); 
   
    


	}

}*/