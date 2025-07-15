local main = {
    name = "Join",
    version = "1.0.0"
}

print("§a[Chat] Script de join")
print("§a[Autor] Neomin")
print("§a[Versão] " .. main["version"])

local function onPlayerJoin(event)
    local player = event:getPlayer()
    event.setJoinMessage("§e" .. player:getName() .. " entrou no servidor.")
end

local function onPlayerQuit(event)
    local player = event:getPlayer()
    event.setQuitMessage("§e" .. player:getName() .. " saiu no servidor.")
end


return {
    main = main,
    onPlayerJoin = onPlayerJoin,
    onPlayerQuit = onPlayerQuit
}
