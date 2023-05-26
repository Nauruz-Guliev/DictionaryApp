import * as React from "react";
import {Navigate, useNavigate} from "react-router-dom";
import * as AppEndPoints from "../constants/AppEndPoints";


function Redirect(model) {
    let navigate = useNavigate();

    function handleClick() {
        console.log("CLICKED")
        navigate(AppEndPoints.DICTIONARY, {state: {model: model}})
    }

    return (
        <button className="btn btn-primary" onClick={handleClick}>Open dictionary
        </button>
    );
}

export class DictionaryItem extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            model: props.model,
            success: true
        }
    }


    render() {
        return (
            <div className="card w-25">
                <Redirect word={this.state.model}/>
            </div>
        );
    }
}