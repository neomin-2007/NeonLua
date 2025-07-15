local main = {
    name = "Movable_Sand",
    version = "1.0.0"
}

print("§a[Chat] Script de Areia Movediça")
print("§a[Autor] Neomin")
print("§a[Versão] " .. main["version"])

local function onPlayerMove(event)
    local player = event:getPlayer()
    local location = player:getLocation()

    local tag = Tags()
    local current = event.getCurrentMillis()

    local block = location:clone():subtract(0, 1, 0):getBlock()

    local withoutTag = tag.hasTag(player:getName(), "SAND_MESSAGE_DELAY");
    if not withoutTag then
        tag.putTag(player:getName(), "SAND_MESSAGE_DELAY", (current + 1000))
    end

    local currentDelay = tag.getTag(player:getName(), "SAND_MESSAGE_DELAY")
    local inDelay = currentDelay >= current

    if inDelay then
        return
    end
    
    if block:getType() == Material.SAND then
        player:sendMessage("§ePisando em falso...")
        tag.putTag(player:getName(), "SAND_MESSAGE_DELAY", (current + 1000))

        location:subtract(0, 1, 0)
        player:teleport(location)
    end

end


return {
    main = main,
    onPlayerMove = onPlayerMove
}