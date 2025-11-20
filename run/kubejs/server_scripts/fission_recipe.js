ServerEvents.recipes(event => {
    event.recipes.gtceu.fission_reactor('test_fission')
        .itemInputs("gtceu:uranium_235_dust")
        .itemOutputs("gtceu:lead_dust")
        .addData("thermalNeutrons", 6)
        .addData("fastNeutrons", 4)
        .duration(5*60*20)
})