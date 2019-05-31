import React, { Component} from "react";
import "./SetsPane.scss";
import SetsApi from '../api/SetsApi';

export default
class SetsPane extends Component{
  render(){
    const api = new SetsApi();
    api.list().then(result=>{
      console.log(result);
    });
    return(
      <div className="SetsPane">
        <div>SETS</div>
      </div>
    );
  }
}
