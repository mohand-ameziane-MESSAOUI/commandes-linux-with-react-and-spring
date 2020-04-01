import React from 'react';
import Navbar from './Navbar'
import{BrowserRouter , Route} from 'react-router-dom'
import Home from './Home'
import Accueil from './Accueil'

import './index.css';
//************************************************************************************************//
//cette classe nous permet la navigation entre les défférent fichier à l'aide de la classe Navbar//
//************************************************************************************************//
class Commandes extends React.Component {
   render() {
    const Commandes = () => {
      return (
          <div className="bg"></div>
      );
    }
      return (
        
       <BrowserRouter>
       <div className = "App">
       <Navbar/>
    
       <Route path='/Commandes/Home' component = {Home}/> 
       <Route exact path='/' component = {Accueil}/> 
       </div>
       </BrowserRouter>
       

      )
   }
 }


export default Commandes;
