import React, {Component} from "react";
import "./DataPane.scss";

export default
class EditorPane extends Component {
  state  = { 
    record: this.props.record
  };

  render(){
    return(
      <div className="EditorPane">
        {this.state.record.title}
      </div>
    );
  }

}