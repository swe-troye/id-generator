-- ***** Prepare pseudo data for debugging - Start *****
-- workerTable = {}
-- workerTable[1] = "workers:1:0"
-- workerTable[2] = "workers:1:1"
-- workerTable[3] = "workers:1:2"

-- ARGV = {}
-- ARGV[1] = 1 -- Data Center Id
-- ARGV[2] = 512 -- Max Worker Id
-- ***** Prepare data for debugging - End *****


-- to dump object into string
local function dump(o)
    if type(o) == 'table' then
       local s = '{ '
       for k,v in pairs(o) do
          if type(k) ~= 'number' then k = '"'..k..'"' end
          s = s .. '['..k..'] = ' .. dump(v) .. ','
       end
       return s .. '} '
    else
       return tostring(o)
    end
end

-- Set operations
local function addToSet(set, key)
    set[key] = true
end

local function removeFromSet(set, key)
    set[key] = nil
end

local function containsInSet(set, key)
    return set[key] ~= nil
end

-- Create worker set from worker table
local function createWorkerSet(workers)
    -- use table as a set
    local set = {}
    for key, val in pairs(workers) do
        -- value example -> workers:0:23 -> make it as a key of a set
        addToSet(set, val)
    end
    
    return set;
end

-- ARGV[1] -> Data Center Id
-- ARGV[2] -> Max Worker Id
--   worker example -> workers:[DataCenterId]:[WorkerId] -> workers:1:23
-- ARGV[3] -> Pod Uid as value of a redis string
-- ARGV[4] -> Expiration seconds of a redis string


-- Get keys by pattern (worker table)
local workerTable = redis.call("KEYS", KEYS[1])
local workerSet = createWorkerSet(workerTable)
-- print(containsInSet(workerSet, "workers:1:0"))

local id = 0
for idx = 0, ARGV[2] do
    
    if (not containsInSet(workerSet, "workers:" .. ARGV[1] .. ":" .. idx))
    then
        id = idx
        break
    end
end

local completeWorkerId = "workers:" .. ARGV[1] .. ":" .. id
redis.call("SET", completeWorkerId , ARGV[3])
redis.call("EXPIRE", completeWorkerId, ARGV[4])

-- print("id:" .. id)
return id