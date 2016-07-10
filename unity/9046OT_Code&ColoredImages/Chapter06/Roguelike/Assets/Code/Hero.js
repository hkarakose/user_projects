
var animator: Animator;


var enemy:Rigidbody2D;

var speed:float = 2.0;

var right:boolean = false;
var left:boolean = false;
var up:boolean = false;
var down:boolean = false;


var orb:Rigidbody2D;	
var orbSpeed:float = 20f;  
var orbSpeed2:float = -20f;


function Start () {
animator = GetComponent("Animator");
 
 enemySpawn();
 

}

function OnGUI()
{

	GUI.Box (Rect (10,10,100,90), ""+Time.time);
}
InvokeRepeating("enemySpawn", 3, 3);

function Update () 
{
	
	
		var orbInstance:Rigidbody2D;
		
		if(Input.GetButtonDown("Fire1"))
		{
		


orbInstance = Instantiate(orb, transform.position, Quaternion.Euler(new Vector3(-1,0,0)));

if (right==true) 
{
	orbInstance.velocity = new Vector2(orbSpeed, 0); 
}
if (left==true) 
{
	orbInstance.velocity = new Vector2(orbSpeed2, 0);  
}


if (up==true) 
{
	
	orbInstance.velocity = new Vector2(0, orbSpeed); 
}
if (down==true) 	
	{
		orbInstance.velocity = new Vector2(0, orbSpeed2);  
	}

}
}


function FixedUpdate () 
{
	MoveCharacter();
}


function MoveCharacter()
{


if (Input.GetKey(KeyCode.D))
{
	
	animator.SetBool("left", false);
	animator.SetBool("up", false );
	animator.SetBool("down", false );
	animator.SetBool("right", true );
	down = false;
	left = false;
	right = true;
	up = false;
	transform.Translate(Vector3.right * speed * Time.deltaTime);
}

if (right == true)
{
	transform.Translate(Vector3.right * speed * Time.deltaTime);	
}

if (left == true)
{
	transform.Translate(Vector3.left * speed * Time.deltaTime);	
}

if (up == true)
{
	transform.Translate(Vector3.up * speed * Time.deltaTime);	
}

if (down == true)
{
	transform.Translate(Vector3.down * speed * Time.deltaTime);	
}

if (Input.GetKey(KeyCode.S))
{
	
	animator.SetBool("left", false);
	animator.SetBool("up", false );
	animator.SetBool("right", false );
	animator.SetBool("down", true );
	down = true;
	left = false;
	right = false;
	up = false;
	transform.Translate(Vector3.down * speed * Time.deltaTime);
}
if (Input.GetKey(KeyCode.A))
{

	animator.SetBool("down", false );
	animator.SetBool("right", false );
	animator.SetBool("up", false);
	animator.SetBool("left", true );
	down = false;
	left = true;
	right = false;
	up = false;
	transform.Translate(Vector3.left * speed * Time.deltaTime);
}
if (Input.GetKey(KeyCode.W))
{

	animator.SetBool("right", false );
	animator.SetBool("down", false );
	animator.SetBool("left", false);
	animator.SetBool("up", true );
	down = false;
	left = false;
	right = false;
	up = true;
	transform.Translate(Vector3.up * speed * Time.deltaTime);
}
}

function OnCollisionEnter2D(other:Collision2D){
  	if((other.gameObject.name=="enemy(Clone)")||(other.gameObject.name=="right")||(other.gameObject.name=="left")||(other.gameObject.name=="bottom")||(other.gameObject.name=="top")) //bullet or bullet clones collide with enemy 
  	{
  	Time.timeScale = 0;
    Destroy(gameObject); 


	}

}

function enemySpawn()
{
var enemyInstance:Rigidbody2D;


	enemyInstance = Instantiate(enemy, Vector3(Random.Range(2, 8),Random.Range(-4, 4),0), Quaternion.Euler(new Vector3(0,0,0)));

}

