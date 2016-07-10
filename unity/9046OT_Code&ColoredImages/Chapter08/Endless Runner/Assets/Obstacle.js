var obstacle:Rigidbody2D;
var speed:float = -3.0; 


var ninjaObj:GameObject;

function Start () 
{

}

function Update () 
{
	
	
}
Invoke("SpawnObstacle", 3);

function SpawnObstacle()
{

	var obstacleInstance:Rigidbody2D; 

obstacleInstance = Instantiate(obstacle, Vector3(10,Random.Range(-4, 0),0), Quaternion.Euler(new Vector3(0,0,0)));
obstacleInstance.name = "Obstacle(Clone)";
obstacleInstance.velocity = new Vector2(speed, 0);

}



function OnCollisionEnter2D(other:Collision2D)
{

	
  	if(other.gameObject.name=="Ninja")
  	{
  	    Time.timeScale = 0;
    Destroy(gameObject); 
   
    


	}

}