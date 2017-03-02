import { pandaFetch } from './pandaFetch';
import { uriEncodeParams } from '../util/uriEncodeParams';

export default {

  searchTags: function(searchText) {
    return pandaFetch(
      `/support/previewCapi/tags?q=${searchText}`,
      {
        method: 'get',
        credentials: 'same-origin'
      }
    )
    .then((res) => res.json())
    .then((json) => Promise.resolve(json.response.results));
  },

  searchAtoms: function(query) {
    return pandaFetch(
      `/support/previewCapi/atoms?${uriEncodeParams(query)}`,
      {
        method: 'get',
        credentials: 'same-origin'
      }
    )
    .then((res) => res.json())
    .then((json) => {
      return Promise.resolve(json.response.results);
    });
  },

  getAtomUsages: function(atomId, atomType) {
    return pandaFetch(
      `/support/previewCapi/atom/${atomType}/${atomId}/usage`,
      {
        method: 'get',
        credentials: 'same-origin'
      }
    )
    .then((res) => res.json())
    .then((json) => {
      return Promise.resolve(json.response.results);
    });
  },

  getByPath: function(path) {
    return pandaFetch(
      `/support/previewCapi/${path}?show-fields=all`,
      {
        method: 'get',
        credentials: 'same-origin'
      }
    )
    .then((res) => res.json())
    .then((json) => {
      return Promise.resolve(json.response.results);
    });
  }

};
