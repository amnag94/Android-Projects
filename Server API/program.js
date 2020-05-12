var express = require('express');
var app = express();
var users = require('./user');

//import Ingredients from './users';

var ingredients = new users.Ingredients();

app.get('/info', (req, res) => {
    res.type('json');
    res.send(ingredients.get_ingredients());
});

app.get('/info/:ingred', (req, res) => {
    res.type('json');
    res.send(ingredients.get_ingredient(req.params.ingred));
});

app.put('/info/:ingred/:measure/:quantity', (req, res) => {
    result = ingredients.add_ingredients(req.params.ingred, req.params.measure, req.params.quantity);
    res.send(`${req.params.ingred} ${result}`);
});

app.listen(3000);