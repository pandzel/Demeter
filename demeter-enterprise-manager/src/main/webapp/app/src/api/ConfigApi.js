import axios from 'axios';

export default
class ConfigApi {
  load() {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/config`).then((result)=>resolve(result.data));
    });
  }
}