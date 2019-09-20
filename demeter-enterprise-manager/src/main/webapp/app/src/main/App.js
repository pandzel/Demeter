import React, { Component} from "react";
import "./App.scss";
import 'primereact/resources/themes/rhea/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';

import Header from "../layout/Header";
import Stage  from "../layout/Stage";
import Footer from "../layout/Footer";

export default
class App extends Component{
  render(){
    return(
      <div className="App">
        <Header/>
        <Stage/>
        <Footer/>
      </div>
    );
  }
}
