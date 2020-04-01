import React from 'react';
import Center from 'react-center';
import 'react-dropdown/style.css'
import { Button, Jumbotron} from 'react-bootstrap';
import logo from './img/logo.jpg';
import logo2 from './img/logo2.png';
import background from './img/background.jpg';
//****************************************************//
//cette classe est la classe d'accueil de notre site //
//**************************************************//
var sectionStyle = {
  width: "100%",
  height: "400px",
  backgroundImage: `url(${logo})`
};


class Accueil extends React.Component {
 
    click(){
      window.location.href = "http://localhost:3000/Commandes/Home";
    }
   render() {
     
     return (
<div style={{  backgroundColor :'beige',
      height : '1000px',
      width : '100%',
      
      }}>
     
     
       
  <div className = 'center' >
     <div className ="row">
       <div className ="col">
        <img src={logo2} alt="logo2" style = {{widh :'100px',height: '200px'}} />;
      </div>
    </div> 
    <Center>
       <h1 className = "center">Hello !!</h1>
    </Center>
    <Center>
       <p><b style ={{color : 'black'}}> Utiliser le terminal linux et essayer à chaque fois de se rappeler des commandes pour avancer est plutot barbant hein
                      ? No Problem ! consultez ce site, vous serez etonné 😉 </b></p>
    </Center>
                    
    
  </div>
</div>
  
     )
  }
}


export default Accueil;
