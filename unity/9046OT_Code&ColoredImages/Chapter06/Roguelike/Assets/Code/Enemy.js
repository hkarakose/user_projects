

var heroObj:GameObject;

var enemyRight:boolean = false;
var enemyLeft:boolean = false;
var enemyUp:boolean = false;
var enemyDown:boolean = false;
var enemySpeed:float;
var enemyAnimator: Animator; 



InvokeRepeating("Accelerate", 2, 5);

function Start()
{
enemySpeed = 1.0;
}

function OnCollisionEnter2D(other:Collision2D)
{
  	if(other.gameObject.name=="orb(Clone)")
  	{
  	
  		Destroy(other.gameObject); 
    	Destroy(gameObject); 
    	
    	

	}

}

function Update()
{
	enemyMove();
	
}


function enemyMove()
{
	
	heroObj = GameObject.Find("hero");
	
	enemyAnimator = GetComponent("Animator"); 
	if (heroObj != null)
	{
	if (transform.position.y > heroObj.transform.position.y)
	{
		
			enemyAnimator.SetBool("enemyLeft", false);  
			enemyAnimator.SetBool("enemyUp", false );    
			enemyAnimator.SetBool("enemyDown", true );  
			enemyAnimator.SetBool("enemyRight", false );
			enemyDown = true;  
			enemyLeft = false;   
			enemyRight = false;
			enemyUp = false;    
		 	transform.Translate(Vector3.down * enemySpeed * Time.deltaTime); 
		 	
	}
	else
	{
		
			enemyAnimator.SetBool("enemyLeft", false);  
			enemyAnimator.SetBool("enemyUp", true );  
			enemyAnimator.SetBool("enemyDown", false ); 
			enemyAnimator.SetBool("enemyRight", false );
			enemyDown = false;  
			enemyLeft = false;  
			enemyRight = false;
			enemyUp = true; 
			transform.Translate(Vector3.up * enemySpeed * Time.deltaTime); 
	}
	
	if (transform.position.x < heroObj.transform.position.x)
	{
			
			enemyAnimator.SetBool("enemyLeft", false); 
			enemyAnimator.SetBool("enemyUp", false ); 
			enemyAnimator.SetBool("enemyDown", false );   
			enemyAnimator.SetBool("enemyRight", true ); 
			enemyDown = false;  
			enemyLeft = false; 
			enemyRight = true; 
			enemyUp = false;
		 	transform.Translate(Vector3.right * enemySpeed * Time.deltaTime); 
	}
	else
	{
			
			enemyAnimator.SetBool("enemyLeft", true); 
			enemyAnimator.SetBool("enemyUp", false ); 
			enemyAnimator.SetBool("enemyDown", false ); 
			enemyAnimator.SetBool("enemyRight", false );
			enemyDown = false; 
			enemyLeft = true;  
			enemyRight = false;
			enemyUp = false; 
			transform.Translate(Vector3.left * enemySpeed * Time.deltaTime); 
	}
	}
}

function Accelerate()
{
enemySpeed = enemySpeed + 1;
}



			
	



