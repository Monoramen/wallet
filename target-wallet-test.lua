math.randomseed(os.time())

local wallets = {
    "3c8e7b2a-1d4f-4a6c-9b32-7e5f8c3a9d01",
    "a2b4c6d8-e0f2-4a8c-6e8a-1c3e5f7a9c2e",
    "8f7e5d3c-1b2a-4c6e-9d8f-3a5c7e9f1b4d",
    "4d6f8a2c-9e1b-4f7a-8c3d-5a9e7f1b3c5d",
    "1a3c5e7f-9b2d-4f8a-6c1e-3d5f7a9c1e3f",
    "7e9c1a3f-5b8d-4c2a-6e1f-3a7c9e1b5d7f",
    "2b4d6f8a-1c3e-5f7a-9c2e-4d6f8a2c9e1b",
    "9e1b3d5f-7a9c-4e2b-6f8a-1d3f5a7c9e1b",
    "5c7e9a1c-3d5f-4b8a-6e1f-7c9a1d3f5b7e",
    "3a5c7e9f-1b4d-4a8c-6e2f-9b1d3f5a7c9e",
    "8d6f4a2c-9e1b-5f7a-8c3d-4a9e7f1b3c5d",
    "1e3f5a7c-9b2d-4f8a-6c1e-5d3f7a9c1e3f",
    "7c9a1d3f-5b7e-4c2a-6e1f-3a7c9e1b5d7f",
    "4a6c8e2a-1f3d-5b7a-9c4e-6d8f2a1c4e6f",
    "2d4f6a8c-1e3b-5f7a-9c2d-4f6a8c1e3b5f",
    "9b1d3f5a-7c9e-4a2b-6f8c-1d3f5a7c9e1b",
    "6e8a2c4e-1f3d-5b7a-9c6e-8a2c4e1f3d5b",
    "3f5a7c9e-1b2d-4f8a-6c3f-5a7c9e1b2d4f",
    "8a2c4e6f-1d3b-5f7a-9c8a-2c4e6f1d3b5f",
    "5d7f9a1c-3e5b-4f8a-6c5d-7f9a1c3e5b4f",
    "1c3e5f7a-9b2d-4a8c-6e1c-3e5f7a9b2d4a",
    "7a9c1e3f-5b7d-4f2a-6c7a-9c1e3f5b7d4f",
    "4e6f8a2c-1d3b-5f7a-9c4e-6f8a2c1d3b5f",
    "2b5d7f9a-1c3e-4f8a-6b2d-5f7a9c1e3f4a",
    "9c1e3f5a-7b9d-4f2a-6c9e-1f3a5c7b9d2f",
    "6a8c2e4f-1d3b-5f7a-9c6a-8c2e4f1d3b5f",
    "3d5f7a9c-1e2b-4f8a-6c3d-5f7a9c1e2b4f",
    "8c2e4f6a-1d3b-5f7a-9c8c-2e4f6a1d3b5f",
    "5a7c9e1b-3d2f-4f8a-6c5a-7c9e1b3d2f4a",
    "1e2b4d6f-8a3c-5f7a-9c1e-2b4d6f8a3c5f",
    "7c9e1b3d-5f2a-4f8a-6c7e-9b1d3f5a2c4e",
    "4a2c4e6f-8a1d-3f5b-7a9c-4e6f8a2c1d3f",
    "2d3f5a7c-9e1b-4f8a-6c2d-3f5a7c9e1b4f",
    "9e1b3d5f-7a2c-4f8a-6c9e-1b3d5f7a2c4f",
    "6c8a2e4f-1d3b-5f7a-9c6c-8a2e4f1d3b5f",
    "3f5a2c4e-6f8a-1d3b-5f7a-9c4e6f8a2c1d",
    "8a2e4f6c-1d3b-5f7a-9c8a-2e4f6c1d3b5f",
    "5d7f9a2c-3e5b-4f8a-6c5d-7f9a2c3e5b4f",
    "1c3e5f7a-9b2c-4f8a-6e1c-3e5f7a9b2c4f",
    "7a9c1e3f-5b7c-4f2a-6e7a-9c1e3f5b7c4f",
    "4e6f8a2c-1d3c-5f7a-9e4e-6f8a2c1d3c5f",
    "2b5d7f9a-1c3c-4f8a-6e2b-5d7f9a1c3c4f",
    "9c1e3f5a-7b9c-4f2a-6e9c-1e3f5a7b9c2f",
    "6a8c2e4f-1d3c-5f7a-9e6a-8c2e4f1d3c5f",
    "3d5f7a9c-1e2c-4f8a-6e3d-5f7a9c1e2c4f",
    "8c2e4f6a-1d3c-5f7a-9e8c-2e4f6a1d3c5f",
    "5a7c9e1b-3d2c-4f8a-6e5a-7c9e1b3d2c4f",
    "1e2b4d6f-8a3c-5f7a-9e1e-2b4d6f8a3c5f",
    "7c9e1b3d-5f2c-4f8a-6e7c-9e1b3d5f2c4f",
    "4a2c4e6f-8a1d-3f5c-7a9e-4e6f8a2c1d3f"
}

local shuffled_wallets = {}
for i, v in ipairs(wallets) do
    shuffled_wallets[i] = v
end

function shuffle(t)
    for i = #t, 2, -1 do
        local j = math.random(i)
        t[i], t[j] = t[j], t[i]
    end
end

shuffle(shuffled_wallets)
local index = 1

request = function()
    local walletId = shuffled_wallets[index]
    index = index + 1
    if index > #shuffled_wallets then
        index = 1
        shuffle(shuffled_wallets)
    end


    local operation = math.random(1, 10) <= 8 and "DEPOSIT" or "WITHDRAW"
    local amount = math.random(1, 50)

    local body = string.format('{"walletId": "%s", "operationType": "%s", "amount": %d}', walletId, operation, amount)
    local headers = { ["Content-Type"] = "application/json" }

    return wrk.format("POST", "/api/v1/wallet", headers, body)


end

response = function(status, headers, body)
    if status >= 400 then
        print("Error status: "..status.." body: "..body)
    end
end
