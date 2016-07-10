#pragma strict

var power:float = 20.0;
var speed:float = 3.0;
var camSpeed:float = 100.0;
var lavaSpeed:float = 100.0;
var spacebuddy:Rigidbody2D;
var cam:GameObject;
var lava:GameObject;
var jumpclip: AudioClip;
var winclip: AudioClip;
var loseclip: AudioClip;
var score: float = 0.0;

function Start()
{
	if (Application.loadedLevelName == "scene2")
	{
	AudioSource.PlayClipAtPoint(winclip, transform.position);
		score = PlayerPrefs.GetFloat("score",0);
	}
}

function OnGUI()
{
	GUI.Box (Rect (10,10,100,30), "Score: "+score);
}

function FixedUpdate()
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

function OnTriggerEnter2D(other:Collider2D)
{
  	if(other.gameObject.name=="Platform")
  	{
  		score=score+50.0;
  		PlayerPrefs.SetFloat("score", score);
  		AudioSource.PlayClipAtPoint(jumpclip, transform.position);
  	    spacebuddy.velocity = new Vector2(0,power);
  	    cam = GameObject.Find("Camera");
  	    lava = GameObject.Find("Lava");
  	
  	    cam.transform.Translate(Vector3.up * camSpeed * Time.deltaTime);
  	    lava.transform.Translate(Vector3.up * lavaSpeed * Time.deltaTime);
  	   
	}
	if(other.gameObject.name=="Lava")
  	{
  		AudioSource.PlayClipAtPoint(loseclip, transform.position);
  		Time.timeScale = 0;
  	}
  	if(other.gameObject.name=="UFObeam")
  	{
  		
  		Application.LoadLevel("Scene2");
  		
  		
  	}
  	if(other.gameObject.name=="UFObeam2")
  	{
  		AudioSource.PlayClipAtPoint(winclip, transform.position);
  		Time.timeScale = 0;
  		
  		
  		
  	}
}