import React from 'react'
import {NavLink} from 'react-router-dom'
//***********************************************************************//
//cette classe est la classe qui nous construit le Navbar et son contenu//
//**********************************************************************//
const Navbar = ()=>{
    return(
        <nav className = "nav-wrapper darken-3" style ={{backgroundColor : 'red'}}>
        <div className = "container">
      
        <ul className = "right">
        
        <li><NavLink to = "/Commandes/Home">Home</NavLink ></li>
        <li><NavLink to ="/">Accueil</NavLink></li>
       
        </ul>
        </div>
        
        </nav>
    )
}
export default Navbar