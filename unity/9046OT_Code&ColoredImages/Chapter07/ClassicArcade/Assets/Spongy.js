#pragma strict

var speed:float = 1.0; 
var lives:int = 3;
var score:int = 0;

var myStyle:GUIStyle;
function Start () 
{

}


function OnGUI()
{
	GUI.Box (Rect (10,10,100,30), "Time: "+Time.time, myStyle);
	GUI.Box (Rect (500,10,100,30), "Score: "+score);
	GUI.Box (Rect (600,10,100,30), "Lives: "+lives);
}

function Update () 
{
	MoveCharacter();
	
}

function MoveCharacter()
{

if (Input.GetKey(KeyCode.A))
{
	transform.Translate(Vector3.left * speed * Time.deltaTime);
}

if (Input.GetKey(KeyCode.D))
{
	transform.Translate(Vector3.right * speed * Time.deltaTime);
}
}

var gameObjects : GameObject[];
 
function RemovalGrime()
{
 
    gameObjects =  GameObject.FindGameObjectsWithTag("Grime");
 
    for(var i = 0 ; i < gameObjects.length ; i ++)
        Destroy(gameObjects[i]);
}
 
function RemovalAcid()
{
 
    gameObjects =  GameObject.FindGameObjectsWithTag("Acid");
 
    for(var i = 0 ; i < gameObjects.length ; i ++)
        Destroy(gameObjects[i]);
}

function OnCollisionEnter2D(other:Collision2D)
{


	
  	if(other.gameObject.name=="Acid(Clone)")
  	{
  	 lives = lives - 1;
  	 Destroy(other.gameObject);
  	
        
    if (lives == 0)
    {
    RemovalAcid();
    RemovalGrime();
    Time.timeScale = 0;
    	
    }

	}
	
	if(other.gameObject.name=="Grime(Clone)")
  	{
  		score = score + 50;
  		Destroy(other.gameObject);
  		
  	}

}


