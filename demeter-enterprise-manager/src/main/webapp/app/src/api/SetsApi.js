import axios from 'axios';

export default
class SetsApi {
  list() {
    return new Promise((resolve, reject) => {
      axios.get(`${config.context}rest/sets`).then((result)=>resolve(result.data));
    });
  }
}