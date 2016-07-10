var platform : RuntimePlatform = Application.platform;
var control : String;

function checkTouch(pos){


    var wp : Vector3 = Camera.main.ScreenToWorldPoint(pos);
    var touchPos : Vector2 = new Vector2(wp.x, wp.y);
    var hit = Physics2D.OverlapPoint(touchPos); 
 
    if(hit){
    control = hit.transform.name;
    if (control == "down")
    {

  	move();
    }
    }
}

function Update()
{
 if(platform == RuntimePlatform.Android || platform == RuntimePlatform.IPhonePlayer){
       if(Input.touchCount > 0) {
         if(Input.GetTouch(0).phase == TouchPhase.Began){
          checkTouch(Input.GetTouch(0).position);
         }
       }
    }else if(platform == RuntimePlatform.WindowsEditor){
       if(Input.GetMouseButton(0)) {
         checkTouch(Input.mousePosition);
       }
    }
}


function move()
{
	
	ship = GameObject.Find("Ship");
	
 	ship.transform.Translate(Vector3.down * (Time.deltaTime*10), Camera.main.transform);
	
}
