import React, { Component} from "react";
import "./Sets.scss";
import SetsApi from '../api/SetsApi';

export default
class Sets extends Component{
  render(){
    const api = new SetsApi();
    api.list().then(result=>{
      console.log(result);
    });
    return(
      <div className="Sets">
        <div>SETS</div>
      </div>
    );
  }
}
