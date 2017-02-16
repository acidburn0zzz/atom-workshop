import React, { PropTypes } from 'react';

import {ManagedForm, ManagedField} from '../../ManagedEditor';
import FormFieldNumericInput from '../../FormFields/FormFieldNumericInput';
import FormFieldArrayWrapper from '../../FormFields/FormFieldArrayWrapper';
import FormFieldTextInput from '../../FormFields/FormFieldTextInput';
import FormFieldImageSelect from '../../FormFields/FormFieldImageSelect';
import {RecipeServings} from './RecipeFields/Servings';

export class RecipeEditor extends React.Component {

  static propTypes = {
    atom: PropTypes.shape({
      type: PropTypes.string
    }).isRequired,
    onUpdate: PropTypes.func.isRequired,
    config: PropTypes.shape({
      gridUrl: PropTypes.string.isRequired
    }).isRequired
  }

  render () {

    return (
      <div className="editor editor-recipe">
        <ManagedForm data={this.props.atom} updateData={this.props.onUpdate}>
          <ManagedField fieldLocation="data.recipe.time.preparation" name="Preparation Time (mins)">
            <FormFieldNumericInput/>
          </ManagedField>
          <ManagedField fieldLocation="data.recipe.time.cooking" name="Cooking Time (mins)">
            <FormFieldNumericInput/>
          </ManagedField>
          <ManagedField fieldLocation="data.recipe.serves" name="Serving Information">
            <RecipeServings />
          </ManagedField>
          <ManagedField fieldLocation="data.recipe.steps" name="Steps">
            <FormFieldArrayWrapper>
              <FormFieldTextInput />
            </FormFieldArrayWrapper>
          </ManagedField>
          <ManagedField fieldLocation="data.recipe.images" name="Images">
            <FormFieldArrayWrapper>
              <FormFieldImageSelect gridUrl={this.props.config.gridUrl}/>
            </FormFieldArrayWrapper>
          </ManagedField>
        </ManagedForm>
      </div>
    );
  }
}
