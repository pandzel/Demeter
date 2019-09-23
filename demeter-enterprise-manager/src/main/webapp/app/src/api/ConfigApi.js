import axios from 'axios';

export default
class ConfigApi {
  load() {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/config`).then((result)=>{
        console.log("ConfigApi load()", result);
        resolve(result.data);
      });
    });
  }
  
  save(data) {
    return new Promise((resolve, reject) => {
      axios.post(`${config.context}rest/config`, data).then((result)=>{
        console.log("ConfigApi save()", result);
        resolve(result);
      });
    });
  }
}