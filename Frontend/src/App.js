import React from 'react';

import Commandes from './Commandes';

class App extends React.Component {
click(){
  window.location.href = "http://localhost:3000/Commandes/Home";
}
  render() {
  
   
   return (
 
     
     <Commandes/>
    
     )
  }
}

export default App;
