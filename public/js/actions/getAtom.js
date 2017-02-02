import AtomsApi from '../../services/AtomsApi';

function requestAtom(id, atomType) {
  return {
    type:       'ATOM_GET_REQUEST',
    id:         id,
    atomType:   atomType,
    receivedAt: Date.now()
  };
}

function receiveAtom(atom) {
  return {
    type:       'VIDEO_GET_RECEIVE',
    atom:       atom,
    receivedAt: Date.now()
  };
}

function errorReceivingAtom(error) {
  console.error(error);
  return {
    type:       'SHOW_ERROR',
    message:    'Could not get atom',
    error:      error,
    receivedAt: Date.now()
  };
}

export function getAtom(id, atomType) {
  return dispatch => {
    dispatch(requestAtom(id, atomType));
    return AtomsApi.fetchAtom(id, atomType)
        .then(atom => {
          dispatch(receiveAtom(atom));
        })
        .catch(error => dispatch(errorReceivingAtom(error)));
  };
}
