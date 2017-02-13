import React from 'react';

export default class FormFieldSelectBox extends React.Component {

  static propTypes = {
    fieldLabel: React.PropTypes.string.isRequired,
    fieldName: React.PropTypes.string.isRequired,
    fieldValue: React.PropTypes.string.isRequired,
    selectValues: React.PropTypes.array.isRequired
  };

  renderOption(option) {
    return (
        <option key={option} value={option}>{option}</option>
    )
  }

  renderOptions() {
    return this.props.selectValues.map(this.renderOption);
  }

  render() {
    return (
        <div>
          <label htmlFor={this.props.fieldName} className="form__label">{this.props.fieldLabel}</label>
          <select className="form__field form__field--select" value={this.props.fieldValue} onChange={this.props.onUpdateField}>
            {this.renderOptions()}
          </select>
        </div>
    );
  };
}