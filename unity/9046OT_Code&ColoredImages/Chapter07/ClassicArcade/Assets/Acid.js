#pragma strict
var acid:Rigidbody2D;
var speed:float = -3.0; 


var spongyObj:GameObject;

function Start () 
{

}

function Update () 
{
	
	
}
Invoke("SpawnAcid", 0.5);

function SpawnAcid()
{

	var acidInstance:Rigidbody2D; 

acidInstance = Instantiate(acid, Vector3(Random.Range(-8, 8),7,0), Quaternion.Euler(new Vector3(0,0,0)));
acidInstance.name = "Acid(Clone)";
acidInstance.velocity = new Vector2(0, speed);

}



/*function OnCollisionEnter2D(other:Collision2D)
{

	
  	if(other.gameObject.name=="Spongy")
  	{
  	
    Destroy(gameObject); 
   
    


	}

}*/