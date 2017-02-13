import React from 'react';

export default class FormFieldTextInput extends React.Component {

  static propTypes = {
    fieldLabel: React.PropTypes.string.isRequired,
    fieldName: React.PropTypes.string.isRequired,
    fieldValue: React.PropTypes.string.isRequired,
    fieldPlaceholder: React.PropTypes.string,
    onUpdateField: React.PropTypes.func.isRequired
  };

  render() {
    return (
        <div>
          <label htmlFor={this.props.fieldName} className="form__label">{this.props.fieldLabel}</label>
          <input type="text" className="form__field" id={this.props.fieldName} placeholder={this.props.fieldPlaceholder || ''} value={this.props.fieldValue} onChange={this.props.onUpdateField}/>
        </div>

    );
  }
};