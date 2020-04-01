import React from 'react';
import Center from 'react-center';
import axios from 'axios';
import 'react-dropdown/style.css'
import {Button ,Jumbotron,Row,Col} from 'react-bootstrap';
import Select from 'react-select'; 
import 'bootstrap/dist/css/bootstrap.min.css';
import logo from './img/logo.jpg';
import background from './img/background.jpg';


//****************************************//
//cette classe est la classe principale //
//*************************************//
var sectionStyle = {
  width : "100%",
  height:"400px",
  backgroundImage:"url("+{logo}+")"

};
var stylearea = {
  width : "400px",
  height:"160px",
  background :'white',
  color :'black'
  

};
var couleur ={
  background :'white',
  color :'black'
}
var desc ={
  background :'white',
  color :'black',
  width : "400px",
  height:"68px",

}
var couleur2 ={

  color :'black'
}
var fs = require('fs');
let payload = { 
  "cmd": "", 
  "opt": [], 
  "nbfile": 1, 
  "result": "resultat" 
} 
//****************************************//
//ses variables on les utilise dans les requêtes http//
//parce qu'on ne peut pas utiliser les variables //
//de constructeur dans les requêtes. //
//*************************************//
 var opts =['O'];
 var com =[];
 var tabJson=[];
 var tabRepense=[];
 let id = '';
var File =[];
 var chaineOptions = [];
 var data =[];
 var t ="400";
 var index3 = 0;
 var nbopt = 0;
 var res ='';
 var res2 ='';
 var view ='';
var message = 'loading...';
 
 
class Home extends React.Component {
   constructor() {
      super();
    this.state = {
      test : '',
      newOption :'option',
      newCommand : 'command',
      Id:'',
      command :com,
      options: opts,
      render :false,
      position : 0,
      value : '',
      File:[],
      nomFichier : '',
      description:'description des options',
      viewfile : data,
      messageRes: ''
    };
  //*************************************************************//
  //on déclare ses méthodes dans le constructeur pour qu'on      //
  //puisse appeler les states de constructeur dedans.            //
  //*************************************************************//
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.onchangeNameFile = this.onchangeNameFile.bind(this);
    this.supprimerOptions = this.supprimerOptions.bind(this);
    this.optionsCommand = this.optionsCommand.bind(this);
    this.envoyer = this.envoyer.bind(this);
    this.afficheResultat = this.afficheResultat.bind(this);
    this.Demander = this.Demander.bind(this);
   
    }
  //*************************************************************//
  //cette méthode permet de récupérer et de parser le fichier    //
  //JSON automatiquement sans appeler la méthode                 //
  //*************************************************************//
   async componentDidMount(){  
      console.log("bonjour1");
      const res = await fetch('/commands')
       .then(function(response){
        
          return response.json();
         
       }).then((j)=> {
      
          tabJson =  JSON.parse(JSON.stringify(j)) ;
          
          for (var index = 0; tabJson[index]; index++) {
            com[index]={label :(JSON.parse(JSON.stringify(j[index].cmd))),value:index};
            console.log(com);
            
          }index3=0; 
          this.affichecom() ;
         
       });
  }
  //*************************************************************//
  //cette méthode permet de récupérer le fichier parcouru        //
  //*************************************************************//
    
  Parcourir(){
    data = new FormData();
    var imagedata = document.querySelector('input[type="file"]').files[0];
    data.append("file", imagedata);
  }
  //*************************************************************//
  //cette méthode permet de récupérer le fichier résultat        //
  //*************************************************************//
  async resultat(){
  var url1 = "/resultat/"+id
  const res = await axios({
   url: url1, //your url
   method: 'GET',
   responseType: 'blob', // important
 }).then((response) => {
   view= JSON.stringify(response.data);
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', payload.result); //or any other extension
    document.body.appendChild(link);
    let fileReader = new FileReader(url);
    
    link.click();
 
    });}
  //*************************************************************//
  //cette méthode permet de récupérer le statut de résultat      //
  //*************************************************************//
  async resulstatus(){
    const res = await fetch('/resultstatus/'+id)
     .then(function(response){
      
        return response.json();
       
     }).then((j)=> {
    
        tabRepense =  JSON.parse(JSON.stringify(j)) ;
        if (JSON.parse(JSON.stringify(j.Type))=="ok"){
   
           message = JSON.parse(JSON.stringify(j.Message));
           this.afficheResultat();
   
            this.resultat();
   
   ;}
        else {
         message = JSON.parse(JSON.stringify(j.Message));
         this.afficheResultat();
        }
        return j;
       });
   }
  //******************************************************************//
  //cette méthode permet de récupérer le statut d'execution de server //
  //*****************************************************************//
  async Demander(){
     
    const res = await fetch('/getstatus/'+id)
    .then((response)=>{
      t=response.status;
      if(t =="400"){this.Demander();}else{
      this.resulstatus();
       return response;
      
    }});
    return (<div>{t}</div>);
  }
 
  //***********************************************************************************************//
  //cette méthode permer de récupérer un ID et d'envoyer un fichier JSON (de la ligne de commande) //
  //et envoyer le fichier parcouru ainsi que de récupérer le résultat                              //
  //***********************************************************************************************//
   envoyer= event=>{
    axios({ 
    method: 'post', 
    url: '/execute', 
    data: payload, 
    headers: { 
    
    'Content-Type': 'application/json' 
    }, 
}).then((response) => { 
  id = response.data.id;

  var url = "/execute/"+response.data.id ;
fetch(url,{
    mode: 'no-cors',
    method: "POST",
  
    body: data
  }).then( (res) => {
  
    var repeat = 1;
   
      this.Demander() ;
    
  
      });
  
      }) 
      .catch((error) => { 
      alert(error) 
      })  
    }
  //**************************************************************************//
  //cette méthode permet de récupérer les options de la commande sélectionner //
  //**************************************************************************//
    optionsCommand(event){
    for (var index = 0; tabJson[index]; index++) {
     
       for (var index2 = 0; tabJson[index].opt[index2]; index2++) {
         
         
         if ( com[index].label == event.label){
           
           opts[index3] ={ label :(tabJson[index].opt[index2].name),value:index2};
           index3++;
         } else{
           index2++;
         }
       }
     }
     index3 =0; 
     this.setState({ option: opts});
    }

    handleChange(event){
     this.setState({value: event.value})
    }
    handleSubmit(event) {
      alert('Your favo rite flavor is: ' + this.state.command);
      event.preventDefault();
    }
  //********************************************************//
  //cette méthode permet de supprimer les options choisis  //
  //******************************************************//
    supprimerOptions(event){
      var der = chaineOptions.pop();
      var cmpt =this.state.position
      if(cmpt>0 && nbopt>0){
      this.setState({position: cmpt-1})
      nbopt--;}
      this.majligneCommand() ;
      
    }
   
  //**************************************************************************************//
  //cette méthode permet de récupérer la commande et les optiosn choisis et les afficher //
  //************************************************************************************//
    majligneCommand(){
      res =res2; 
      for( var i = 0;chaineOptions[i];i++){
        res = res+' '+chaineOptions[i];
      }
    }
  //*************************************************************//
  //cette méthode permet de données un nom au fichier à recevoir //
  //************************************************************//
    onchangeNameFile(event){
      this.setState({nomFichier: event.target.value})
      payload.result = this.state.nomFichier
    }
  //*************************************************************************//
  //cette méthode permet de gérer les options à chaque ajout ou modification //
  //************************************************************************//
    onChangeOption= event => {
      chaineOptions[this.state.position] = event.label;
      var cmpt =this.state.position;
      this.setState({position:  cmpt+1});
      payload.opt[nbopt] = event.label;
      
    
      
      this.majligneCommand();
     
      nbopt++;
      this.setState({newOption:event.label}); 
      for (var index = 0; tabJson[index]; index++) {
        for (var index2 = 0; tabJson[index].opt[index2]; index2++) {
         if ( tabJson[index].opt[index2].name == event.label){
            this.setState({description:tabJson[index].opt[index2].description});
             index3++;
          }
        }
      }
       index3 =0; 
       
    }
  //***************************************************************************//
  //cette méthode permet de gérer les commandes à chaque ajout ou modification //
  //**************************************************************************//
    onChangeCommand= event => {
     
      opts.splice(0,opts.length);
      chaineOptions.splice(0,chaineOptions.length);
      nbopt=0;
      res2 = event.label;
      this.setState({position :0})
      res=res2;
      
     
      payload.cmd = event.label;
      this.setState({newCommand:event.label});
      this.optionsCommand(event);
      
    }
  //**************************************************************//
  //cette méthode permet d'afficher le résultat ou l'erreur reçu  //
  //*************************************************************//
  afficheResultat(){
    
      this.setState({messageRes :message});
    }
    affichecom(){
      
      this.setState({command :com});
    }
render() {
     
return (
<div style={{  backgroundColor : "beige",
         height :"1000px",
          backgroundPosition: 'center',
          backgroundSize: 'cover',
          backgroundRepeat: 'no-repeat',
  }}>
     
  <div className = 'container' >
          <div>&nbsp;</div>
          <div>&nbsp;</div>
         
       <h1  style ={couleur2} className = "center">Hello !!</h1>
    <Center>
      <div className = 'center'>
                     
                   <h4 style = {couleur2} >  veuillez selectionner la commande souhaiter ainsi que l'option à éxecuter </h4>
                    <div>&nbsp;</div>
                    <div>&nbsp;</div>
            <div className ="row">
                <div  style ={couleur2} className ="col">
                    choisir une commande
                     <form classeName="pure-form">
                        <Select 
                            style ={couleur}
                            
                            options = {this.state.command}
                            onChange={this.onChangeCommand} /> 
                      </form>
                </div>
                <div  style ={couleur2} classeName ="col">
                     choisir les options
                    <form classeName="pure-form">
                       <Select 
                         style ={couleur}
                       
                          options = {this.state.options}
                          onChange={this.onChangeOption} />
                    </form> 
                     
                </div>
                   &nbsp;&nbsp;
                <div  style ={couleur2} classeName ="col">
                     Description
                    <form classeName="pure-form">
                       <textarea style = {desc} value={this.state.description} onChange={this.handleChange} />
                    </form>
                </div>
           </div>
      <Center>
        <Row>
           <Col>

             <div  style ={couleur2} classeName ="row">
                      commande et options choisi
                 <form classeName="pure-form">
                    <input style = {couleur} type = "text" Value={res}/>
                  </form>
              </div>
               <div  style ={couleur2} classeName ="row">
                 <form classeName="pure-form">
                   <Button style = {{backgroundColor : '#FF7373'}} onClick= {this.supprimerOptions}>supprimer option</Button>
                  </form>
               </div>
               <div  style ={couleur2} classeName ="row">
                    nom du fichier de résultat
                   <form classeName="pure-form">
                      <input type = "text" style = {couleur} Value={this.state.nomFichier} onChange = {this.onchangeNameFile}/>
                    </form>
                </div>
                         
                 <div classeName ="row">
          
                   <form classeName="pure-form">
                      <input type = "file" name = "file" onChange={this.Parcourir}/>
                   </form>
                 </div>
                 <div>&nbsp;</div>
                    <div>&nbsp;</div>
                 <div classeName ="row">
          
                    <form classeName="pure-form">
                        <Button  style = {{backgroundColor : '#FF7373'}} onClick= {this.envoyer}>envoyer</Button>
                    </form>
                  </div>

              </Col>
                     &nbsp;&nbsp;&nbsp;
               <Col>
                    <div  style ={couleur2} classeName ="row">
                        Sortie standard
                        <form classeName="pure-form">
                            <textarea style ={stylearea} value={this.state.messageRes} />
                        </form>
                    </div>
                   
                </Col>
        </Row>
      </Center>
     </div>
    </Center>
  </div>

 
</div>
          
         
     )
  }

}

export default Home;