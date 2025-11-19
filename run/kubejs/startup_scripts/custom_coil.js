// priority: 0

// Visit the wiki for more info - https://kubejs.com/

StartupEvents.registry('block', event => {
    event.create('nak_moderator_block', 'gnt:moderator')
        .maxTemp(1200)
        .heatCapacity(40)
        .fastNeutronConversion(.4)
        .texture('kubejs:block/casing/moderator/nak')
        .hardness(4)
        .requiresTool(true)
})

console.info('Hello, World! (Loaded startup scripts)')

