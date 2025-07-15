local main = {
    name = "Join",
    version = "1.0.0"
}

local function onPlayerJoin(event)
    local player = event:getPlayer()
    player:setWalkSpeed(0.5)
    player:sendMessage("§eO jogador " .. player:getName() .. " entrou no servidor!")
end

print("§a[Join] Script de entrada")
print("§a[Autor] Neomin")
print("§a[Versão] " .. main["version"])

return {
    main = main,
    onPlayerJoin = onPlayerJoin
}