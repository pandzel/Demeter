import React, { Component} from "react";
import "./DataPane.scss";
import RecordsApi from '../api/RecordsApi';

export default
class DataPane extends Component{
  
  componentDidMount() {
    const api = new RecordsApi();
    api.list().then(result => {
      console.log(result);
    });
  }
  
  render(){
    return(
      <div className="DataPane">
        <div>DATA</div>
      </div>
    );
  }
}
