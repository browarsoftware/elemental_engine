function onTalk(javaObj)
  print(type(javaObj) .. " " .. tostring(javaObj))
  print(javaObj.name)
  javaObj:talk()
  return true
end

function onWalk(javaObj)
	javaObj:SetName('dupa')
  javaObj:walk()
  return 1, "km"
end