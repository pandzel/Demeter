import React, {Component} from "react";
import "./SetsPane.scss";

export default
class SetsTable extends Component{
  constructor(props) {
    super(props);
  }
  
  render(){
    const items = this.props.data && this.props.data.map(item => 
    <li key={item.id}><span>{item.setName}</span> [<span>{item.setSpec}</span>]</li>
    );
    return(
      <div className="SetsTable">
        <ul>
          {items}
        </ul>
      </div>
    );
  }
}
