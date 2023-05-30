import * as React from "react";
import {Navigate, useNavigate} from "react-router-dom";
import * as AppEndPoints from "../constants/AppEndPoints";
import {getAuthToken, getUser} from "../auth/axios_helper";
import {useContext} from "react";
import {UserContext} from "../context/Context";


function Redirect(model) {
    let navigate = useNavigate();
    let [user, setUser] = useContext(UserContext);

    function handleClick() {
        navigate(AppEndPoints.DICTIONARY, {state: {model: model}})
    }

    return (
        <div> {getAuthToken() && user &&
            <button className="btn btn-primary w-100" onClick={handleClick}>Open dictionary
            </button>
        }
        </div>
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