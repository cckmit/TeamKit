/* demo1 */
SELECT *
FROM client;

/* TestMapper.eventOf */
select *
from stored_event
#if(id)
where id = #{id}
#end
#if(eventId)
  and event_id =
#{eventId}
#end