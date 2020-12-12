
export class Effect{
	//////////////////////////////////////////////////////////////
    // BUILDER
    //////////////////////////////////////////////////////////////
    constructor(variable){
    	this.variable= variable;
        this.project= variable.project;
        this.data= this.project.data;
        this.all();
    }
    
    //////////////////////////////////////////////////////////////
    // METHODS
    //////////////////////////////////////////////////////////////
    /**
     * 
     */
    all(){
    	this.transform();
    }
    
    /**
     * 
     */
    transform(){
    	let effect= `sofia-effect-transform`;
    	let label= `${effect}__label`;
    	let active= `${label}-active`;
    	let realce= `${effect}-realce`;
    	
    	let element = [...document.getElementsByClassName(effect)];
    	if (element.length > 0 && element != null){
            element.forEach((e, i) => {
            	e.setAttribute(this.data.parent, `${i}`);
            	let children= [...e.children];
        		let children_label = "";
            	children.forEach((c, j) =>{
            		if(c.classList.contains(label)){
            			children_label= c;
            		}
            		if(c.nodeName == "INPUT"){
            			c.addEventListener("focus", (event) =>{
            				let value= event.target.value;
            				if (value === "") {
            					children_label.classList.remove(`${realce}`);
                			} else if (value !== "") {
                				children_label.classList.add(`${realce}`);
                			}
                    	});
            			c.addEventListener("keyup", (event) =>{
            				let value= event.target.value;
            				if (value === "") {
            					children_label.classList.remove(`${active}`,`${realce}`);
                			} else if (value !== "") {
                				children_label.classList.add(`${active}`,`${realce}`);
                			}
                    	});
            			c.addEventListener("blur", (event) =>{
            				let value= event.target.value;
            				if (value === "") {
            					children_label.classList.remove(`${active}`,`${realce}`);
                			} else if (value !== "") {
                				children_label.classList.add(`${realce}`);
                			}
                    	});
            		}
            	});
            });
        }
    }
}